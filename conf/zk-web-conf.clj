{
 :server-port 8080
 :users {"admin" "hello"}
 :default-node ""
 :g-client-id  "Your Google OAuth2 clientID"
 :g-redirect-url "Your redirect address for OAuh2"
 :g-client-secret "Your Google OAuth2 ClientSecret"
 :zk-slack-hook "Slack hook for posting zk updates to slack"
 :g-hd "your org domain as registered on gmail" ;provide only if you have google mail for your domain and want to restrict login to only ids on this domain.
 :prod-ips (list "ip1" "ip2") ;when changes are made to these ips, update is notified on slack.
}
