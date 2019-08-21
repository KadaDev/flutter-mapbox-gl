package com.mapbox.mapboxgl;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

enum LayerTypes {
    LINE,
    SYMBOL,
}
class LayerOptions {
    final LayerTypes type;
    final String color;

    LayerOptions(String type, Map<String, String> options) {
        if (type.equalsIgnoreCase("line")) {
            this.type = LayerTypes.LINE;
        } else {
            this.type = LayerTypes.SYMBOL;
        }
        if (options.get("color") != null) {
            this.color = options.get("color");
        } else {
            this.color = "#FF00FF";
        }
    }
}

class GeoJsonManager {
    /**
     * Create a symbol manager, used to manage symbols.
     *
     * @param mapboxMap the map object to add symbols to
     * @param style     a valid a fully loaded style object
     */
    final private MapboxMap mapboxMap;
    final private MapView mapView;

    final private Style style;

    public GeoJsonManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
        this.mapView = mapView;
        this.mapboxMap = mapboxMap;
        this.style = style;
    }

    public void addSource(GeoJsonSource source, Map options) throws Exception {
        style.addSource(source);

        final Iterator<Map.Entry<String, Map>> it =  options.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, Map> entry = it.next();
            final LayerOptions layerOptions = new LayerOptions(entry.getKey(), entry.getValue());
            Layer layer;
            if (layerOptions.type == LayerTypes.LINE) {
                layer = new LineLayer("lineLayer-" + source.getId(), source.getId()).withProperties(
                        PropertyFactory.lineColor(Color.parseColor(layerOptions.color))
                );
            }
            else if (layerOptions.type == LayerTypes.SYMBOL) {
                layer = new SymbolLayer("symbolLayer-" + source.getId(),source.getId()).withProperties(
                        PropertyFactory.iconColor(Color.parseColor(layerOptions.color))
                );
            } else {
                throw new Exception("no");
            }
            style.addLayer(layer);
        }
/*
        style.addLayer(new LineLayer("linelayer-" + source.getId(), source.getId()).withProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineOpacity(1.0f),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.parseColor(options.color)


                )));
                */

    }

}