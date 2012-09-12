eventCompileStart = { 
  projectCompiler.srcDirectories << "$basedir/test/common" 
} 
eventAllTestsStart = { 
   classLoader.addURL(new File("$basedir/test/common").toURL()) 
}