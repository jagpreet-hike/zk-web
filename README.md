# zk-web

zk-web is a Web UI of [Zookeeper](http://zookeeper.apache.org), just making it easier to use. Sometimes I really get tired of the command line.
zk-web is written in [clojure](http://clojure.org) with [noir](http://webnoir.org) and [boostrap](http://twitter.github.com/bootstrap/). Currently there're just less than 450 lines clojure code at all. Clojure is really so simple and so elegent!

## Usage

To use zk-web, you need [leiningen](https://github.com/technomancy/leiningen) and git currentlly. (And I'll make a stand-alone package later).
Run the following command:

```bash
git clone https://github.com/hike/zk-web.git
cd zk-web
lein deps # run this if you're using lein 1.x
lein run
```
Meet with zk-web at [http://localhost:8080](http://localhost:8080)! I'am sure it's super easy!

## Configuration

zk-web is also easy to configurate. It reads `$HOME/.zk-web-conf.clj` or `conf/zk-web-conf.clj` when it starts up. As you‘ve already seen, the configuration file is also clojure code. Let's see an example:

```clojure
{
:server-port 8080
:users {"admin" "hello"} ; optional
:default-node ""
:g-client-id  "Your Google OAuth2 clientID"
:g-redirect-url "Your redirect address for OAuh2"
:g-client-secret "Your Google OAuth2 ClientSecret"
:g-hd "your org domain as registered on gmail" ;provide only if you have google mail for your domain and want to restrict login to only ids on this domain.
:zk-ip2group {:ip1 :grp1 :ip2 :grp2 :ip3 :grp1} ;map ips to a group.(both ip and groupname are keywords that is start with a ":" followed by value. Like ":value" without quotes) (here ip1 and ip3 are in grp1
:zk-slack-hooks {:grp1 "Slack hook to post changes to zk for grp1" :grp2 "Slack hook to post changes to zk for grp2"} ;when changes are made to zk with connection string having ip in group "grp", updates are logged to slack using hook corresponding to ":grp"
:allowed-email-ids #{} ; set of email ids allowed to have access. Works only when :restrict-to-ids is true
:restrict-to-ids false ; set this to true to restrict access to few ids, listed in set :allowed-email-ids
}
```

## Features
* Jump to ancesters of a node in navigation bar.
* List children of a node with link to them.
* Show stat and data of a node.
* Remember last 3 zookeepers you visit in cookie.
* Create/edit/delete/rmr a node.
* Simple authority management.
* Default node for first-arrival guest.

## TODO
* Data Format - Format json, xml and so on.

## Contributers
* @lra
* @lispmind
* @killme2008


## License

Copyright (C) 2012

Distributed under the Eclipse Public License, the same as Clojure.
