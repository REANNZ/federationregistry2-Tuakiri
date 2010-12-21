class ConsoleOutputCodec {
    
    def encode = { def output ->
        return (output?.toString() ?: "").encodeAsHTML().replaceAll(/[\n\r]/,"<br/>")
    }
    
}