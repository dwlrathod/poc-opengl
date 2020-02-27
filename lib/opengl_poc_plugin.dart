import 'dart:async';

import 'package:flutter/services.dart';

class OpenglPocPlugin {
  static const MethodChannel _channel =
      const MethodChannel('opengl_poc_plugin');
  int textureId;

  Future<int> initialize(double width, double height) async {
    textureId = await _channel.invokeMethod('create', {
      'width': width,
      'height': height,
    });
    return textureId;
  }

  Future<Null> dispose() =>
      _channel.invokeMethod('dispose', {'textureId': textureId});

  bool get isInitialized => textureId != null;
}
