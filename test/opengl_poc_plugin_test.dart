import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:opengl_poc_plugin/opengl_poc_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('opengl_poc_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await OpenglPocPlugin.platformVersion, '42');
  });
}
