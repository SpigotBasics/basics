# Messages
All messages files support using a list of strings or a single string. They also support MiniMessage, PlaceholderAPI and
user-defined variables (see more on that later). To disable a message, set it to an empty list `([])`:

```yaml
# Single line message with user defined variable $dm$
join-message: "<gold>%player_name%</gold> <rainbow>has joined the game!</rainbow> $dm$"

# Multi line message
join-message-self:
  - "<gold>Welcome back, %player_name%.</gold>"
  - "<gold>Your IP address is <rainbow>%player_ip%</rainbow> and you're in world <rainbow>%player_world%</rainbow></gold>"

# No message
leave-message: []
```