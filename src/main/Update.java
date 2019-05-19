package main;

import java.util.List;

public class Update {
    private int versionNumber;
    private int version;
    private String describe;
    private List<UpdateSource> updateSource;

    public int getVersionNumber() {
        return versionNumber;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<UpdateSource> getUpdateSource() {
        return updateSource;
    }

    public void setUpdateSource(List<UpdateSource> updateSource) {
        this.updateSource = updateSource;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    class UpdateSource {
        private String sourceName;
        private String updateURL;

        public String getUpdateURL() {
            return updateURL;
        }

        public void setUpdateURL(String updateURL) {
            this.updateURL = updateURL;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        @Override
        public String toString() {
            return "UpdateSource{" +
                    "sourceName='" + sourceName + '\'' +
                    ", updateURL='" + updateURL + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "以下是Udater信息：\n" +
                "versionNumber=" + versionNumber +
                ", version=" + version +
                ", describe='" + describe + '\'' +
                ", updateSource=" + updateSource ;
    }
}
