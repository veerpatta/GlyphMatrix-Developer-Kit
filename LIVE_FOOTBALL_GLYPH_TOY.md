# Live Football Glyph Toy

A sample Glyph Toy concept that automatically surfaces live football scores for favourite teams and animates key match moments (goals, half-time, full-time) on the Glyph Matrix.

## Experience goals
- Show the current score and match state when the device is face-down or locked.
- Trigger a short goal celebration animation when any favourite team scores.
- Provide low-distraction periodic score updates while minimising battery and data usage.

## Data flow
1. A lightweight worker fetches scores from a football API every 60 seconds during live matches and every 10 minutes otherwise.
2. Parsed match data is stored in a small in-memory cache keyed by fixture ID.
3. The Glyph Toy service renders one of three states:
   - **Idle preview**: team crests and next kickoff time.
   - **Live score**: abbreviated team names, scoreline, and match clock.
   - **Goal celebration**: a burst animation that plays for ~3 seconds before returning to the live score frame.
4. The service stops polling when no favourite team has an active fixture.

## Glyph layout
- **Top stripe**: Home score (left) and away score (right) using numeric glyph assets.
- **Middle matrix**: Animated clock text (e.g., `72'`) that increments locally between API polls.
- **Bottom stripe**: Team initials (e.g., `ARS` vs `MCI`).
- **Goal animation**: A radial ripple or sweeping bar across the matrix, layered behind the static score to keep orientation clear.

## Example assets
Use 1:1 bitmap assets sized for the matrix grid. Suggested set:
- Digits `0-9` for scores.
- Small three-letter abbreviations for team names.
- A ripple spritesheet (`goal_ripple_0` … `goal_ripple_4`) for the celebration.

## Service outline (Java)
```java
public class LiveFootballToyService extends Service {
    private GlyphMatrixManager gm;
    private Handler handler = new Handler(Looper.getMainLooper());
    private MatchCache cache = new MatchCache();

    @Override
    public IBinder onBind(Intent intent) {
        init();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopUpdates();
        gm.turnOff();
        gm.unInit();
        gm = null;
        return false;
    }

    private void init() {
        gm = GlyphMatrixManager.getInstance(getApplicationContext());
        gm.init(new GlyphMatrixManager.Callback() {
            @Override
            public void onServiceConnected(ComponentName name) {
                gm.register(Glyph.DEVICE_23112);
                startUpdates();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) { }
        });
    }

    private void startUpdates() {
        handler.post(updateRunnable);
    }

    private void stopUpdates() {
        handler.removeCallbacks(updateRunnable);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            MatchState state = cache.refresh(); // Fetch and parse API payload.
            render(state);
            handler.postDelayed(this, state.isLive() ? 60_000 : 600_000);
        }
    };

    private void render(MatchState state) {
        GlyphMatrixFrame.Builder frameBuilder = new GlyphMatrixFrame.Builder();

        GlyphMatrixObject homeScore = new GlyphMatrixObject.Builder()
                .setImageSource(Assets.scoreDigit(state.getHomeScore()))
                .setPosition(0, 0)
                .build();

        GlyphMatrixObject awayScore = new GlyphMatrixObject.Builder()
                .setImageSource(Assets.scoreDigit(state.getAwayScore()))
                .setPosition(GlyphMatrixSpec.WIDTH - 12, 0)
                .build();

        GlyphMatrixObject clock = new GlyphMatrixObject.Builder()
                .setText(state.clockText())
                .setPosition(6, 18)
                .build();

        GlyphMatrixObject teams = new GlyphMatrixObject.Builder()
                .setText(state.teamLine())
                .setPosition(0, 32)
                .build();

        frameBuilder.addTop(homeScore).addTop(awayScore).addMiddle(clock).addBottom(teams);

        if (state.justScored()) {
            GlyphMatrixObject ripple = new GlyphMatrixObject.Builder()
                    .setImageSource(Assets.goalRipple(state.celebrationFrame()))
                    .setPosition(0, 0)
                    .setBrightness(200)
                    .build();
            frameBuilder.addBackground(ripple);
        }

        gm.setMatrixFrame(frameBuilder.build().render());
    }
}
```

### Notes
- `MatchCache.refresh()` should debounce identical scorelines to avoid unnecessary renders.
- When a goal is detected, play 3–4 `goalRipple` frames in quick succession (e.g., 120 ms per frame) before returning to the live layout.
- Respect AOD behaviour: dim brightness and slow the poll cadence when in always-on state.

## Manifest entry
Register the toy in `AndroidManifest.xml` with a preview image and localized name:
```xml
<meta-data
    android:name="com.nothing.glyph.toy.name"
    android:resource="@string/toy_name_live_football"/>
<meta-data
    android:name="com.nothing.glyph.toy.image"
    android:resource="@drawable/img_live_football_preview"/>
```
