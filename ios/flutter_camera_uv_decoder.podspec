#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_camera_uv_decoder'
  s.version          = '0.0.1'
  s.summary          = 'Flutter Camera'
  s.description      = <<-DESC
A Flutter plugin to use the camera from your Flutter app.
                       DESC
  s.homepage         = 'https://github.com/flutter/plugins'
  s.license          = { :type => 'BSD', :file => '../LICENSE' }
  s.author           = { 'Flutter Dev Team' => 'flutter-dev@googlegroups.com' }
  s.source           = { :http => 'https://sk-global.biz' } # Unpublish
  s.documentation_url = 'https://sk-global.biz' # Unpublish
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'

  s.vendored_frameworks    = 'Framework/VoiceCodeDecoder.framework', 'Framework/opencv2.framework'
  s.frameworks              = 'AssetsLibrary', 'CoreMedia', 'CoreVideo', 'AVFoundation', 'QuartzCore', 'CoreGraphics', 'Foundation', 'AudioToolbox', 'CoreMotion'
  s.static_framework = true

  s.platform = :ios, '11.0'
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'VALID_ARCHS[sdk=iphonesimulator*]' => 'x86_64' }
end
