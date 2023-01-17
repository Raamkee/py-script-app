import 'dart:io' as io;

import 'package:flutter/material.dart';

import 'package:path_provider/path_provider.dart';

class ListFilesScreen extends StatefulWidget {
  const ListFilesScreen({super.key});

  @override
  State<ListFilesScreen> createState() => ListFilesScreenState();
}

class ListFilesScreenState extends State<ListFilesScreen> {
  //Declare Globaly
  late String directory;
  List file = [];
  @override
  void initState() {
    super.initState();
    _listofFiles();
  }

  // Make New Function
  void _listofFiles() async {
    directory = (await getApplicationDocumentsDirectory()).path;
    debugPrint(directory);
    setState(() {
      file = io.Directory("$directory/")
          .listSync(); //use your folder name insted of resume.
    });
  }

  // Build Part
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Get List of Files with whole Path"),
      ),
      body: Column(
        children: <Widget>[
          // your Content if there
          Expanded(
            child: ListView.builder(
              itemCount: file.length,
              itemBuilder: (BuildContext context, int index) {
                return Text(file[index].toString());
              },
            ),
          )
        ],
      ),
    );
  }
}
