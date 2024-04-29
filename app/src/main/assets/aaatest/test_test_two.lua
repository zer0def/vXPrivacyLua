function before(hook, param)
    log("IN TEST FUNCTION (2) EXECTRANSACT")
    --param:printStack()
    param:printBinder()
    return false
end