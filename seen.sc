// seen.sc
// Provides: /seen <player>
// Data file: world/scripts/seen.data.nbt
// By: wh0oo

global_seen = {};
global_cooldowns = {};
global_cooldown_seconds = 10;

__config() -> {
  'scope' -> 'global',
  'stay_loaded' -> true,
  'command_permission' -> 'all',
  'commands' -> {
    '' -> 'usage',
    '<name>' -> 'seen'
  },
  'arguments' -> {
    'name' -> {
      'type' -> 'term',
      'suggester' -> _(args) -> map(values(global_seen), _:'name')
    }
  }
};

__on_start() -> (
  data = load_app_data();
  global_seen = if(data, parse_nbt(data), {});
  for(player('all'), record_player(_, true, false))
);

__on_close() -> (
  for(player('all'), record_player(_, false, false));
  save_seen()
);

save_seen() -> (
  store_app_data(encode_nbt(global_seen, true))
);

usage() -> (
  c = player();
  if(c, print(c, 'Usage: /seen <player>'), print('Usage: /seen <player>'))
);

timestamp(t) -> (
  d = convert_date(t);
  str(
    '%04d-%02d-%02d %02d:%02d:%02d UTC',
    d:0, d:1, d:2, d:3, d:4, d:5
  )
);

_check_cooldown(p, seconds) -> (
  key = p ~ 'uuid';
  now = unix_time() / 1000;  // unix_time() returns ms in this Carpet build
  last = global_cooldowns:key;
  if(last && (now - last) < seconds,
    remaining = ceil(seconds - (now - last));
    print(p, format('r Wait ', 'e ' + str(remaining) + 's', 'r  before using this again.'));
    false,
    global_cooldowns:key = now;
    true
  )
);

record_player(p, online, trigger_save) -> (
  name = p ~ 'name';
  if(!name, return());
  key = lower(name);
  now = unix_time() / 1000;  // unix_time() returns ms in this Carpet build
  now_text = timestamp(now);
  old = global_seen:key;
  last_join_time = if(online, now, if(old, old:'last_join', now));
  last_join_str = if(online, now_text, if(old, old:'last_join_text', now_text));
  global_seen:key = {
    'name' -> name,
    'uuid' -> p ~ 'uuid',
    'online' -> online,
    'last_seen' -> now,
    'last_seen_text' -> now_text,
    'last_join' -> last_join_time,
    'last_join_text' -> last_join_str
  };
  if(trigger_save, save_seen())
);

__on_player_connects(p) -> (
  record_player(p, true, false)
);

__on_player_disconnects(p, reason) -> (
  record_player(p, false, true)
);

seen(name) -> (
  caller = player();
  if(!caller,
    print('This command can only be run by a player.');
    return()
  );
  if(!_check_cooldown(caller, global_cooldown_seconds),
    return()
  );
  key = lower(name);
  rec = global_seen:key;
  if(!rec,
    print(caller, str('No seen data for %s.', name));
    return()
  );
  if(rec:'online',
    print(caller, str('%s is online right now.', rec:'name')),
    print(caller, str('%s was last seen: %s', rec:'name', rec:'last_seen_text'))
  )
);
