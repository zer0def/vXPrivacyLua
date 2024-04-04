function before(hook, param)
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end

    log('[xlog] File being Opened Via Low Level Core IO [open], File => ' .. arg)
    return false
end