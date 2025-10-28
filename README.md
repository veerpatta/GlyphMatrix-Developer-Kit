# Glyph Matrix Developer Kit
The Glyph Matrix Developer Kit provides everything you need to know before creating a custom Glyph Matrix experience **in your app** or **building your own Glyph Toy** on compatible devices.

At its core is the Glyph Matrix Android library that can convert your designs into Glyph Matrix Data and rendering it frame by frame on the Glyph Matrix. It also provides you with identifiers so you can handle events relevant to Glyph Button.

This documentation contains the following three sections
- [**Getting Started**](#getting-started): How to integrate the Glyph Matrix Android Library, configure your development environment and create preview images for your Glyph Toys.
- [**Developing a Glyph Toy Service**](#developing-a-glyph-toy-service): How to manage the service life cycle for Glyph Toy, handle interaction, and behavior of toy service if it has AOD capability.

The following code block shows a full implementation of a Glyph Toy Service in Java. It demonstrate how to initialize the GlyphMatrixManager, register a device, and display a custom GlyphMatrixObject (for example, a butterfly image) on the Glyph Matrix.

```java
@Override
public IBinder onBind(Intent intent) {
    init();
    return null;
}

@Override
public boolean onUnbind(Intent intent) {
    mGM.turnOff();
    mGM.unInit();
    mGM = null;
    mCallback = null;
    return false;
}

private void init() {
    mGM = GlyphMatrixManager.getInstance(getApplicationContext());
    mCallback = new GlyphMatrixManager.Callback() {
        @Override
        public void onServiceConnected(ComponentName componentName) {
            mGM.register(Glyph.DEVICE_23112);
            action();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    mGM.init(mCallback);
}

private void action() {
    GlyphMatrixObject.Builder butterflyBuilder = new GlyphMatrixObject.Builder();
    GlyphMatrixObject butterfly = butterflyBuilder
            .setImageSource(GlyphMatrixUtils.drawableToBitmap(getResources().getDrawable(R.drawable.butterfly)))
            .setScale(100)
            .setOrientation(0)
            .setPosition(0, 0)
            .setReverse(false)
            .build();
    
    GlyphMatrixFrame.Builder frameBuilder = new GlyphMatrixFrame.Builder();
    GlyphMatrixFrame frame = frameBuilder.addTop(butterfly).build();
    mGM.setMatrixFrame(frame.render());
}
```

## Cricket Score Glyph Toy Service

### Overview
The **CricketScoreToyService.java** is a complete implementation of a Glyph Toy Service that displays live cricket scores and match information on the Glyph Matrix. This service demonstrates how to create an engaging, dynamic display that cycles through multiple cricket matches.

### Features
- **Real-time Score Display**: Shows team names, runs, wickets, overs, and match status
- **Multiple Match Support**: Cycles through different cricket matches
- **Auto-rotation**: Automatically rotates through matches at configurable intervals
- **Interactive Navigation**: Manual controls to navigate between matches
- **Formatted Display**: Optimized text layout for Glyph Matrix readability

### Usage

#### Basic Implementation
```java
CricketScoreToyService service = new CricketScoreToyService();
service.initialize();
service.displayScore();  // Display current match
```

#### Navigation Controls
```java
service.nextMatch();        // Move to next match
service.previousMatch();    // Move to previous match
service.getCurrentMatchInfo(); // Get current match details
```

#### Auto-rotation Setup
```java
service.startAutoRotation(30); // Auto-rotate every 30 seconds
```

### Sample Output
The service displays cricket scores in the following format:
```
IND vs AUS
IND: 287/6 (50.0)
AUS: 245/8 (48.2)
IND Won by 42 runs
```

### Demo
Run the main method in `CricketScoreToyService.java` to see a demonstration of:
- Service initialization
- Score display functionality
- Match navigation
- Sample cricket data

### Customization
You can customize the service by:
- **Adding Real Data**: Replace sample data with live cricket APIs
- **Styling Display**: Modify the `formatScoreForGlyph()` method for different layouts
- **Animation Effects**: Add scrolling or blinking effects for enhanced visual appeal
- **Sound Integration**: Include sound effects for boundaries, wickets, or match updates

### Integration with Glyph Matrix
This service follows the standard Glyph Matrix SDK patterns:
- Implements proper initialization and cleanup
- Uses formatted text rendering for the LED matrix
- Handles display updates efficiently
- Supports interactive controls via Glyph buttons

### Other useful resources
For a practical demo project on building Glyph Toys, see the [GlyphMatrix-Example-Project](https://github.com/KenFeng04/GlyphMatrix-Example-Project)

> Kits for building a Glyph Interface experience around devices with a Glyph Light Stripe [Glyph-Developer-Kit](https://github.com/Nothing-Developer-Programme/Glyph-Developer-Kit)

## Support
If you've found an error in this kit, please file an issue.

If there is any problem related to development, you can contact: [GDKsupport@nothing.tech](mailto:GDKsupport@nothing.tech)

However, you may get a faster response from our [community](https://nothing.community/t/glyph-sdk)
