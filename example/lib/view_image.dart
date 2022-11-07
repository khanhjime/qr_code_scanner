import 'dart:io';

import 'package:flutter/material.dart';

class ViewImage extends StatelessWidget {
  final String path;
  final String data;
  const ViewImage({Key? key,required this.path,required this.data}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          children: [
            Image.file(File(path)),
            Text(data ,style: const TextStyle(color: Colors.black,fontSize: 17),)
          ],
        ),
      ),
    );
  }
}
