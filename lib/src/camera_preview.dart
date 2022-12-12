// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter_camera_uv_decoder/flutter_camera_uv_decoder.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

/// A widget showing a live camera preview.
class CameraPreview extends StatelessWidget {
  /// Creates a preview widget for the given camera controller.
  const CameraPreview(this.controller, {this.child});

  /// The controller for the camera that the preview is shown for.
  final CameraController controller;

  /// A widget to overlay on top of the camera preview
  final Widget? child;

  // MODIFIED => Move to class scope
  Map<DeviceOrientation, int> get turns => {
        DeviceOrientation.portraitUp: 0,
        DeviceOrientation.landscapeLeft: 1,
        DeviceOrientation.portraitDown: 2,
        DeviceOrientation.landscapeRight: 3,
      };

  @override
  Widget build(BuildContext context) {
    return controller.value.isInitialized
        ? AspectRatio(
            aspectRatio: _isLandscape()
                ? controller.value.aspectRatio
                : (1 / controller.value.aspectRatio),
            child: Stack(
              fit: StackFit.expand,
              children: [
                _wrapInRotatedBox(child: controller.buildPreview()),
                child ?? Container(),
              ],
            ),
          )
        : Container();
  }

  Widget _wrapInRotatedBox({required Widget child}) {
    // MODIFIED
    if (defaultTargetPlatform != TargetPlatform.android) {
      // iOS
      return RotatedBox(
        quarterTurns: turns[DeviceOrientation.landscapeLeft]!,
        child: child,
      );
    }

    return RotatedBox(
      quarterTurns: _getQuarterTurns(),
      child: child,
    );
  }

  bool _isLandscape() {
    return [DeviceOrientation.landscapeLeft, DeviceOrientation.landscapeRight]
        .contains(_getApplicableOrientation());
  }

  int _getQuarterTurns() {
    return turns[_getApplicableOrientation()]!;
  }

  DeviceOrientation _getApplicableOrientation() {
    return controller.value.isRecordingVideo
        ? controller.value.recordingOrientation!
        : (controller.value.lockedCaptureOrientation ??
            controller.value.deviceOrientation);
  }
}
