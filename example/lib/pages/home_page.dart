import 'package:flutter/material.dart';
import 'package:flutter_camera_uv_decoder_example/main.dart';

class HomePage extends StatefulWidget {
  HomePage({Key? key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Home'),
      ),
      body: ElevatedButton(
        onPressed: () {
          Navigator.of(context).pushNamed(AppRoutes.camera);
        },
        child: Text('Go to camera page'),
      ),
    );
  }
}
