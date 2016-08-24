'use strict';

const version = '18.48.477.13';
const beta = '';

const exec = require('child_process').exec;
const fs = require('fs');
const wget = require('node-wget');

function prepareLibrary(filePath) {
  exec(`unzip -j ${filePath} classes.jar`, [], (error, stdout, stderr) => {
    if (error) console.log(error);
    exec(`zip -d classes.jar javax\\*`, [], (error, stdout, stderr) => {
      if (error) console.log(error);
      exec(`zip -ro ${filePath} classes.jar`, [], (error, stdout, stderr) => {
        if (error) console.log(error);
        fs.unlinkSync(`classes.jar`);
        try {
          fs.unlinkSync(`./libs/${filePath}`);
        } catch (e) {
          console.log('No previous file');
        }
        fs.renameSync(filePath, `./libs/${filePath}`);
        console.log('Library baked');
      });
    });
  });
}

function handleDownloaded(error, data) {
  if (error == null) {
    console.log('File downloaded successfully');
    prepareLibrary(data.filepath);
  } else {
    console.error('Failed downloading file');
    console.error(error);
  }
}

function downloadLibrary() {
  console.log('Downloading file...');
  let url = `https://download.01.org/crosswalk/releases/crosswalk/android/maven2/org/xwalk/xwalk_core_library${beta}/${version}/xwalk_core_library${beta}-${version}.aar`
  wget(url, handleDownloaded);
}

downloadLibrary();
