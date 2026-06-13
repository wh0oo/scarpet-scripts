// https://github.com/wh0oo/scarpet-scripts/edit/main/seen.sc
// seen.sc
// Provides: /seen <player>
// Data file: world/scripts/seen.data.nbt

global_seen = {};

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
  print(player(), 'Usage: /seen <player>')
);

timestamp(t) -> (
  d = convert_date(t);
  str(
    '%04d-%02d-%02d %02d:%02d:%02d UTC',
    d:0, d:1, d:2, d:3, d:4, d:5
  )
);

record_player(p, online, trigger_save) -> (
  name = p ~ 'name';
  if(!name, return());

  key = lower(name);
  now = unix_time();
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
  key = lower(name);
  rec = global_seen:key;

  if(!rec,
    print(player(), str('No seen data for %s.', name));
    return()
  );

  if(rec:'online',
    print(player(), str('%s is online right now.', rec:'name')),
    print(player(), str('%s was last seen: %s', rec:'name', rec:'last_seen_text'))
  )
);
