import 'dart:async';

import 'package:flutter/material.dart';
import 'package:opengl_poc_plugin/opengl_poc_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _controller = new OpenglPocPlugin();
  var _width = 200.0;
  var _height = 200.0;

  @override
  initState() {
    super.initState();

    initializeController();
  }

  @override
  void dispose() {
    _controller.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('OpenGL via Texture widget'),
        ),
        body: Builder(builder: (context) {
          _height = MediaQuery.of(context).size.height -
              Scaffold.of(context).appBarMaxHeight;
          _width = MediaQuery.of(context).size.width;
          return new Container(
            width: _width,
            height: _height,
            child: _controller.isInitialized
                ? new Texture(textureId: _controller.textureId)
                : null,
          );
        }),
        floatingActionButton: new FloatingActionButton.extended(
            onPressed: () => shout(),
            label: new Text("Shout out to Harrys"),
            icon: new Icon(Icons.speaker)),
      ),
    );
  }

  shout() async {
    await OpenglPocPlugin.shout();
  }

  Future<Null> initializeController() async {
    await _controller.initialize(_width, _height);
    setState(() {});
  }
}
