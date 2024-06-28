import java.util.*;
import java.io.*;

public class URLShortener {
    private Map<String, String> urlMap;
    private Map<String, String> hashMap;
    private static final String DATA_FILE = "urlData.txt";
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int HASH_LENGTH = 6;

    public URLShortener() {
        urlMap = new HashMap<>();
        hashMap = new HashMap<>();
        loadFromFile();
    }

    
    private String generateHash(String longUrl) {
        StringBuilder hash = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < HASH_LENGTH; i++) {
            hash.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return hash.toString();
    }

  
    public String shortenURL(String longUrl) throws Exception {
        if (urlMap.containsKey(longUrl)) {
            return urlMap.get(longUrl);
        }
        String shortUrl;
        do {
            shortUrl = generateHash(longUrl);
        } while (hashMap.containsKey(shortUrl));

        urlMap.put(longUrl, shortUrl);
        hashMap.put(shortUrl, longUrl);
        saveToFile();
        return shortUrl;
    }

   
    public String expandURL(String shortUrl) throws Exception {
        if (!hashMap.containsKey(shortUrl)) {
            throw new Exception("Invalid short URL");
        }
        return hashMap.get(shortUrl);
    }

    
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

   
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    urlMap.put(parts[0], parts[1]);
                    hashMap.put(parts[1], parts[0]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        URLShortener urlShortener = new URLShortener();

        while (true) {
            System.out.println("1. Shorten URL");
            System.out.println("2. Expand URL");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            try {
                if (choice == 1) {
                    System.out.print("Enter long URL: ");
                    String longUrl = scanner.nextLine();
                    String shortUrl = urlShortener.shortenURL(longUrl);
                    System.out.println("Short URL: " + shortUrl);
                } else if (choice == 2) {
                    System.out.print("Enter short URL: ");
                    String shortUrl = scanner.nextLine();
                    String longUrl = urlShortener.expandURL(shortUrl);
                    System.out.println("Long URL: " + longUrl);
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
