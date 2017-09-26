package eminem.ren.okhttp3_network_tool.download;


public class FilePoint {
    private String id;// ID
    private String fileName;//文件名
    private String url;//下载地址
    private String filePath;//下载目录


    public FilePoint(String id, String fileName, String url, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
