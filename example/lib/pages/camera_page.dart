import 'dart:developer';

// import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter_camera_uv_decoder/flutter_camera_uv_decoder.dart';
import 'package:flutter_camera_uv_decoder_example/main.dart';
import 'package:flutter_camera_uv_decoder_example/models/uv_rect.dart';

class CameraPage extends StatefulWidget {
  CameraPage({Key? key}) : super(key: key);

  @override
  _CameraPageState createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  CameraController controller = CameraController(
    cameras[0],
    ResolutionPreset.veryHigh,
    enableAudio: false,
    imageFormatGroup: ImageFormatGroup.yuv420,
  );

  UVRect? _rect;

  @override
  void initState() {
    super.initState();

    _init();
  }

  Future<void> _init() async {
    await controller.initialize();

    controller.startImageStream((data) {
      _rect = UVRect(
        tlX: data.tlX,
        tlY: data.tlY,
        trX: data.trX,
        trY: data.trY,
        blX: data.blX,
        blY: data.blY,
        brX: data.brX,
        brY: data.brY,
      );

      final rawResult = data.result?.trim();
      if (rawResult != null && rawResult.isNotEmpty) {
        log('rawResult: $rawResult');
        log('codeHeader: ${data.codeHeader}');
      }

      setState(() {});
    });

    setState(() {});
  }

  @override
  void dispose() {
    super.dispose();

    _disposeCamera();
  }

  Future<void> _disposeCamera() async {
    final date1 = DateTime.now();
    await controller.stopImageStream();
    final date2 = DateTime.now();
    log('Took ${date2.difference(date1).inMilliseconds}');

    controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Camera UV'),
      ),
      body: () {
        if (!controller.value.isInitialized) {
          return Text('Is Loading..');
        }

        return _buildCamera();
      }(),
    );
  }

  Widget _buildCamera() {
    return SingleChildScrollView(
      physics: const NeverScrollableScrollPhysics(),
      child: CameraPreview(
        controller,
        child: Stack(
          children: [
            Container(
              child: () {
                final camPreviewSize = controller.value.previewSize;

                if (camPreviewSize == null) {
                  return SizedBox();
                }

                return CustomPaint(
                  painter: ShapePainter(
                    cameraPreviewSize: camPreviewSize,
                    boundingBox: _rect,
                  ),
                  child: Container(
                    color: const Color(0x1A000000),
                  ),
                );
              }(),
            ),
          ],
        ),
      ),
    );
  }
}

class ShapePainter extends CustomPainter {
  Size cameraPreviewSize;
  UVRect? boundingBox;
  int stroke = 1;

  ShapePainter({
    required this.cameraPreviewSize,
    required this.boundingBox,
  });

  @override
  Future<void> paint(Canvas canvas, Size size) async {
    if (boundingBox == null) return;

    final paint = Paint()
      ..color = Colors.red
      ..strokeWidth = (stroke + 1) * 5
      ..strokeCap = StrokeCap.round;

    // Resolution => High ~ 1280x720 | Landscape mode
    final double cameraWidth = cameraPreviewSize.width;
    final double cameraHeight = cameraPreviewSize.height;

    final double pHeight = size.height;
    final double pWidth = size.width;

    final unwrapEdge = boundingBox!;
    if (unwrapEdge.tlX == null) return;

    final Offset tl = Offset(
      pWidth - (unwrapEdge.tlY! * pHeight / cameraWidth),
      unwrapEdge.tlX! * pWidth / cameraHeight,
    );
    final Offset tr = Offset(
      pWidth - (unwrapEdge.trY! * pHeight / cameraWidth),
      unwrapEdge.trX! * pWidth / cameraHeight,
    );
    final Offset br = Offset(
      pWidth - (unwrapEdge.brY! * pHeight / cameraWidth),
      unwrapEdge.brX! * pWidth / cameraHeight,
    );
    final Offset bl = Offset(
      pWidth - (unwrapEdge.blY! * pHeight / cameraWidth),
      unwrapEdge.blX! * pWidth / cameraHeight,
    );

    canvas.drawLine(tl, tr, paint);
    canvas.drawLine(tr, br, paint);
    canvas.drawLine(br, bl, paint);
    canvas.drawLine(bl, tl, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}
