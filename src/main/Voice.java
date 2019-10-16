package main;

public class Voice {

    class api {

        private String access_token;
        private long expires_in;
        private String refresh_token;
        private String scope;
        private String session_key;
        private String session_secret;
        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
        public String getAccess_token() {
            return access_token;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }
        public long getExpires_in() {
            return expires_in;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
        public String getRefresh_token() {
            return refresh_token;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
        public String getScope() {
            return scope;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }
        public String getSession_key() {
            return session_key;
        }

        public void setSession_secret(String session_secret) {
            this.session_secret = session_secret;
        }
        public String getSession_secret() {
            return session_secret;
        }

    }
}
