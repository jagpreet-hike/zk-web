{
 :server-port 8080
 :users {"admin" "hello"}
 :default-node ""
 :g-client-id  "Your Google OAuth2 clientID"
 :g-redirect-url "Your redirect address for OAuh2"
 :g-client-secret "Your Google OAuth2 ClientSecret"
 :g-hd "your org domain as registered on gmail" ;provide only if you have google mail for your domain and want to restrict login to only ids on this domain.
 :zk-ip2group {:ip1 :grp1 :ip2 :grp2 :ip3 :grp1} ;map ips to a group.(both ip and groupname are keywords that is start with a ":" followed by value. Like ":value" without quotes) (here ip1 and ip3 are in grp1
 :zk-slack-hooks {:grp1 "Slack hook to post changes to zk for grp1" :grp2 "Slack hook to post changes to zk for grp2"} ;when changes are made to zk with connection string having ip in group "grp", updates are logged to slack using hook corresponding to ":grp"
}
