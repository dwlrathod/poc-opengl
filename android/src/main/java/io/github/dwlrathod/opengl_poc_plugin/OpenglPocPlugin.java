package io.github.dwlrathod.opengl_poc_plugin;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.TextureRegistry;

/**
 * OpenglPocPlugin
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class OpenglPocPlugin implements FlutterPlugin, MethodCallHandler {

    private TextureRegistry textures;
    private LongSparseArray<OpenGLRenderer> renders = new LongSparseArray<>();

    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        textures = flutterPluginBinding.getTextureRegistry();
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "opengl_poc_plugin");
        channel.setMethodCallHandler(this);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "opengl_poc_plugin");
        channel.setMethodCallHandler(new OpenglPocPlugin());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        Map<String, Number> arguments = (Map<String, Number>) call.arguments;
        Log.d("OpenglTexturePlugin", call.method + " " + call.arguments.toString());
        if (call.method.equals("create")) {
            TextureRegistry.SurfaceTextureEntry entry = textures.createSurfaceTexture();
            SurfaceTexture surfaceTexture = entry.surfaceTexture();

            int width = arguments.get("width").intValue();
            int height = arguments.get("height").intValue();
            surfaceTexture.setDefaultBufferSize(width, height);

            SampleRenderWorker worker = new SampleRenderWorker();
            OpenGLRenderer render = new OpenGLRenderer(surfaceTexture, worker);

            renders.put(entry.id(), render);

            result.success(entry.id());
        } else if (call.method.equals("dispose")) {
            long textureId = arguments.get("textureId").longValue();
            OpenGLRenderer render = renders.get(textureId);
            render.onDispose();
            renders.delete(textureId);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
