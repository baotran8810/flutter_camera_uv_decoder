import 'dart:async';

// import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter_camera_uv_decoder/flutter_camera_uv_decoder.dart';
import 'package:flutter_camera_uv_decoder_example/pages/camera_page.dart';
import 'package:flutter_camera_uv_decoder_example/pages/home_page.dart';

List<CameraDescription> cameras = [];

class AppRoutes {
  static const home = '/';
  static const camera = '/camera';
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  cameras = await availableCameras();
  runApp(CameraApp());
}

class CameraApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      initialRoute: AppRoutes.home,
      routes: {
        AppRoutes.home: (context) => HomePage(),
        AppRoutes.camera: (context) => CameraPage(),
      },
    );
  }
}
