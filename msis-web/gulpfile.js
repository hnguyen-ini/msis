// Generated on 2017-05-11 using generator-angular 0.16.0
'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var openURL = require('open');
var lazypipe = require('lazypipe');
var rimraf = require('rimraf');
var wiredep = require('wiredep').stream;
var runSequence = require('run-sequence');
var concat = require('gulp-concat');
var connect = require('gulp-connect');
//var webserver = require('gulp-webserver');

var yeoman = {
  app: require('./bower.json').appPath || 'app',
  dist: 'dist'
};

var paths = {
  libjs: [
    'bower_components/jquery/dist/jquery.min.js',
    'bower_components/angular/angular.min.js',
    'bower_components/angular-route/angular-route.min.js',
    'bower_components/angular-cookies/angular-cookies.min.js',
    'bower_components/crypto-js/crypto-js.js',
    'bower_components/toastr/toastr.min.js',
    'bower_components/angular-idle-service/dist/angular-idle-service.js',
    'bower_components/ngstorage/ngStorage.min.js',
    'bower_components/angular-base64/angular-base64.min.js',
    'bower_components/angular-csrf-cross-domain/dist/angular-csrf-cross-domain.min.js',
    'bower_components/bootstrap/dist/js/bootstrap.min.js'
  ],
  libstyle: [
    'bower_components/bootstrap/dist/css/bootstrap.min.css',
    //'bower_components/bootstrap/less/navbar.less',
    //'bower_components/bootstrap/less/navs.less',
    //'bower_components/bootstrap/less/normalize.less',
    'bower_components/toastr/toastr.min.css'
  ],
  scripts: [yeoman.app + '/scripts/**/*.js'],
  styles: [yeoman.app + '/styles/**/*.css'],
  test: ['test/spec/**/*.js'],
  testRequire: [
    yeoman.app + '/bower_components/angular/angular.js',
    yeoman.app + '/bower_components/angular-mocks/angular-mocks.js',
    yeoman.app + '/bower_components/angular-resource/angular-resource.js',
    yeoman.app + '/bower_components/angular-cookies/angular-cookies.js',
    yeoman.app + '/bower_components/angular-sanitize/angular-sanitize.js',
    yeoman.app + '/bower_components/angular-route/angular-route.js',
    'test/mock/**/*.js',
    'test/spec/**/*.js'
  ],
  karma: 'karma.conf.js',
  views: {
    main: yeoman.app + '/index.html',
    files: [yeoman.app + '/views/**/*.html']
  }
};

////////////////////////
// Reusable pipelines //
////////////////////////

var lintScripts = lazypipe()
  .pipe($.jshint, '.jshintrc')
  .pipe($.jshint.reporter, 'jshint-stylish');

var styles = lazypipe()
  .pipe($.autoprefixer, 'last 1 version')
  .pipe(gulp.dest, '.tmp/styles');

///////////
// Tasks //
///////////

gulp.task('styles', function () {
  return gulp.src(paths.styles)
    .pipe(styles());
});

gulp.task('lint:scripts', function () {
  return gulp.src(paths.scripts)
    .pipe(lintScripts())
    .pipe(gulp.dest(yeoman.dist + '/scripts/app.js'));
});

gulp.task('clean:tmp', function (cb) {
  rimraf('./.tmp', cb);
});

gulp.task('start:client', ['start:server'], function () {
  openURL('http://localhost:9000');
});

gulp.task('start:server', function() {
  connect.server({
    root: 'dist',
    livereload: true,
    port: 9000
    // middleware: function(connect, opt) {
    //   return [ historyApiFallback ];
    // }
  });
});

gulp.task('start:html', function () {
  gulp.src('./dist/**/*.html')
    .pipe(connect.reload());
});

gulp.task('start:watch', function () {
  gulp.watch(['./dist/**/*.html'], ['start:html']);
});

gulp.task('start', function(){
  runSequence(['start:server', 'watch']);
}); 

// gulp.task('webserver', function() {
//   gulp.src('dist')
//     .pipe(webserver({
//       root: 'dist',
//       port: 9000,
//       livereload: true,
//       directoryListing: true,
//       open: true
//     }));
// });

gulp.task('stop:server', function(){
  connect.serverClose();
});

gulp.task('start:server:test', function() {
  $.connect.server({
    root: ['test', yeoman.app, '.tmp'],
    livereload: true,
    port: 9001
  });
});

gulp.task('watch', function () {
  $.watch(paths.styles, function(){
     gulp.run('style');
  });

  $.watch(paths.views.main, function(){
    gulp.run('index');
  });
  
  $.watch(paths.views.files, function(){
    gulp.run('view');
  });
    
  $.watch(paths.scripts, function(){
    gulp.run('script');
  });
   
  $.watch(paths.test)
    .pipe($.plumber())
    .pipe(lintScripts());

  gulp.watch('bower.json', ['bower']);
});

gulp.task('serve', function (cb) {
  runSequence('clean:tmp',
    ['lint:scripts'],
    ['start:client'],
    'watch', cb);
});

gulp.task('serve:prod', function() {
  $.connect.server({
    root: [yeoman.dist],
    livereload: true,
    port: 9000
  });
});

gulp.task('test', ['start:server:test'], function () {
  var testToFiles = paths.testRequire.concat(paths.scripts, paths.test);
  return gulp.src(testToFiles)
    .pipe($.karma({
      configFile: paths.karma,
      action: 'watch'
    }));
});

// inject bower components
gulp.task('bower', function () {
  return gulp.src(paths.views.main)
    .pipe(wiredep({
      directory: yeoman.app + '/bower_components',
      ignorePath: '..'
    }))
  .pipe(gulp.dest(yeoman.app + '/views'));
});

///////////
// Build //
///////////

gulp.task('clean:dist', function (cb) {
  rimraf('./dist', cb);
});

gulp.task('client:build', ['html', 'styles'], function () {
  var jsFilter = $.filter('**/*.js');
  var cssFilter = $.filter('**/*.css');

  return gulp.src(paths.views.main)
    .pipe($.useref({searchPath: [yeoman.app, '.tmp']}))
    .pipe(jsFilter)
    .pipe($.ngAnnotate())
    .pipe($.uglify())
    .pipe(jsFilter.restore())
    .pipe(cssFilter)
    .pipe($.minifyCss({cache: true}))
    .pipe(cssFilter.restore())
    .pipe($.rev())
    .pipe($.revReplace())
    .pipe(gulp.dest(yeoman.dist));
});

gulp.task("index", function(){
  return gulp.src(paths.views.main)
    .pipe(gulp.dest(yeoman.dist));
});

gulp.task('view', function () {
  return gulp.src(yeoman.app + '/views/**/*')
    .pipe(gulp.dest(yeoman.dist + '/views'));
});

gulp.task("style", function(){
  return gulp.src(paths.styles)
    .pipe(gulp.dest(yeoman.dist + "/styles"));
});

gulp.task('images', function () {
  return gulp.src(yeoman.app + '/images/**/*')
    .pipe($.cache($.imagemin({
        optimizationLevel: 5,
        progressive: true,
        interlaced: true
    })))
    .pipe(gulp.dest(yeoman.dist + '/images'));
});

gulp.task('copy:extras', function () {
  return gulp.src(yeoman.app + '/*/.*', { dot: true })
    .pipe(gulp.dest(yeoman.dist));
});

gulp.task('copy:fonts', function () {
  return gulp.src(yeoman.app + '/libs/fonts/*')
    .pipe(gulp.dest(yeoman.dist + '/libs/fonts'));
});

gulp.task('script', function(){
  return gulp.src(paths.scripts)
    //.pipe(concat('webapp.js'))
    .pipe(gulp.dest(yeoman.dist + '/scripts'))
});

gulp.task("libjs", function(){
  return gulp.src(paths.libjs)
    .pipe(gulp.dest(yeoman.dist + '/libs/js'))
});

gulp.task('libstyle', function(){
  return gulp.src(paths.libstyle)
    .pipe(gulp.dest(yeoman.dist + '/libs/styles'));
});

gulp.task('build', ['clean:dist'], function () {
  runSequence(['index', 'view', 'style' ,'images', 'copy:fonts' ,'script' ,'libjs', 'libstyle']);
});

gulp.task('default', ['build']);


