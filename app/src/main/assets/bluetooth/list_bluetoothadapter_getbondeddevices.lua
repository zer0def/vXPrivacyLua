function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local sz = param:getContainerSize(res)
    if sz == 0 then
        log("Bluetooth Bonded Devices Returned 0....")
        return false
    end

    log("Bluetooth Devices List Size=" .. sz)
    --filterDevices(Set<BluetoothDevice> devices, List<String> allowList)
    local setting = param:getSetting("bluetooth.allowed.bonded.list", "*")
    if setting == nil then
        log("Bluetooth allow list setting is NULL..")
        return false
    end

    log("Allowed Bluetooth Devices=" .. setting)
    local allowed = param:stringToList(setting, ",")

    local filtered = param:filterSavedBluetoothDevices(res, allowed)
    log("Done filtering Bluetooth Devices")
    local newSz = param:getContainerSize(filtered)
    log("Setting Filtered Bluetooth Devices list size=" .. newSz)
    param:setResult(filtered)
    return true
end