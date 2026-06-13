// ping.sc
// Provides: /ping
// Optional: /ping <player>
__config() -> {
  'scope' -> 'global',
  'command_permission' -> 'all',
  'commands' -> {
    '' -> 'ping_self',
    '<name>' -> 'ping_other'
  },
  'arguments' -> {
    'name' -> {
      'type' -> 'term',
      'suggester' -> _(args) -> map(player('all'), str(_))
    }
  }
};

_require_player(p) -> (
  if(!p, print('This command can only be run by a player.'));
  p
);

ping_self() -> (
  p = _require_player(player());
  if(p,
    print(p, format('w Pong! Your ping is ', 'e ' + query(p, 'ping') + ' ms'))
  )
);

ping_other(name) -> (
  caller = _require_player(player());
  if(!caller, return());
  target = player(name);
  if(!target,
    print(caller, format('r Player not found: ', 'w ' + name)),
    print(caller, format('w ' + name + '\'s ping is ', 'e ' + query(target, 'ping') + ' ms'))
  )
);
