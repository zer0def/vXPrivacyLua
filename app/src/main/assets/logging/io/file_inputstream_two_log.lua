function before(hook, param)
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end

    local fId = param:getFileDescriptorId(arg)
    local num = '0'
    if fId ~= nil then
        num = tostring(fId)
    end

    log('[xlog] File being Streamed FileInputStream(FileDescriptor) => ' .. num)
    return false
end