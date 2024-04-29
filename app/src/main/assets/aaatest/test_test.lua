function before(hook, param)
    log("IN TEST FUNCTION")
    param:printStack()
    return false
end