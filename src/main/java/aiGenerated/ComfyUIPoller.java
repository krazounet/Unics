package aiGenerated;

public final class ComfyUIPoller {

    private ComfyUIPoller() {}

    public static String waitForImageFile(
        ComfyUIClient client,
        String promptId,
        long timeoutMs,
        long pollIntervalMs
    ) throws Exception {

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {

            String historyJson = client.getHistory(promptId);

            String filename =
                ComfyUIHistoryParser.extractImageFilename(
                    historyJson,
                    promptId
                );

            if (filename != null) {
                System.out.println("🟢 Image file ready: " + filename);
                return filename;
            }

            System.out.println("⏳ Waiting for image file...");
            Thread.sleep(pollIntervalMs);
        }

        throw new RuntimeException("Timeout waiting for image file");
    }
}
