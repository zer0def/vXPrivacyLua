
function before(hook, param)
    log("IN TEST FUNCTION (3) TRANSACT")
    --param:printStack()
    param:printBinderProxy()
    return false
end