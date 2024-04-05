function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local setting = param:getSetting("bluetooth.state")
    if setting == nil then
        return false
    end


    if setting == "12" then
        log("Setting Bluetooth IsEnabled State to true")
        param:setResult(true)
        return true
    end

    log("Setting Bluetooth IsEnabled State to false")
    param:setResult(false)
    return true
end