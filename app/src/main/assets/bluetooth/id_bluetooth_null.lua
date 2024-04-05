function before(hook, param)
    log("Bluetooth Value to NULL")
    param:setResult(null)
    return true
end