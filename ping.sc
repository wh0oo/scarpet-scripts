// ping.sc
// Provides: /ping
// Optional: /ping <player>
global_cooldowns = {};
global_cooldown_seconds = 3;

__config() -> {
  'scope' -> 'global',
  'command_permission' -> 'all',
  'commands' -> {
    '' -> 'ping_self',
    '<name>' -> 'ping_other'
  },
  'arguments' -> {
    'name' -> {
      'type' -> 'players',
      'single' -> true
    }
  }
};

_require_player(p) -> (
  if(!p, print('This command can only be run by a player.'));
  p
);

_check_cooldown(p, seconds) -> (
  key = p ~ 'uuid';
  now = unix_time() / 1000;  // unix_time() returns ms in this Carpet build
  last = global_cooldowns:key;
  if(last && (now - last) < seconds,
    remaining = ceil(seconds - (now - last));
    print(p, format('r Wait ', 'e ' + remaining + 's', 'r  before using this again.'));
    false,
    global_cooldowns:key = now;
    true
  )
);

ping_self() -> (
  p = _require_player(player());
  if(p && _check_cooldown(p, global_cooldown_seconds),
    print(p, format('w Pong! Your ping is ', 'e ' + query(p, 'ping') + ' ms'))
  )
);

ping_other(name) -> (
  caller = _require_player(player());
  if(caller && _check_cooldown(caller, global_cooldown_seconds),
    name = str(name);
    target = player(name);
    if(!target,
      print(caller, format('r Player not found: ', 'w ' + name)),
      print(caller, format('w ' + name + '\'s ping is ', 'e ' + query(target, 'ping') + ' ms'))
    )
  )
);
