package io.github.dwlrathod.opengl_poc_plugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

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
    private Context context;
    private LongSparseArray<OpenGLRenderer> renders = new LongSparseArray<>();

    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        textures = flutterPluginBinding.getTextureRegistry();
        context = flutterPluginBinding.getApplicationContext();
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
        Log.d("Plugin Method: ", call.method + " ");
        if (call.arguments != null)
            Log.d("Plugin Arguments: ", call.arguments.toString());

        switch (call.method) {
            case "create": {
                TextureRegistry.SurfaceTextureEntry entry = textures.createSurfaceTexture();
                SurfaceTexture surfaceTexture = entry.surfaceTexture();

                int width = arguments.get("width").intValue();
                int height = arguments.get("height").intValue();
                surfaceTexture.setDefaultBufferSize(width, height);

                SampleRenderWorker worker = new SampleRenderWorker();
                OpenGLRenderer render = new OpenGLRenderer(surfaceTexture, worker);

                renders.put(entry.id(), render);

                result.success(entry.id());
                break;
            }
            case "dispose": {
                long textureId = arguments.get("textureId").longValue();
                OpenGLRenderer render = renders.get(textureId);
                render.onDispose();
                renders.delete(textureId);
                break;
            }
            case "shout": {
                if (context != null)
                    Toast.makeText(context, "Hey, Harrys!", Toast.LENGTH_LONG).show();
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
