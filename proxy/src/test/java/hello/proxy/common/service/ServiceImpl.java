package hello.proxy.common.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceImpl implements ServiceInterface {
    @Override
    public void save() {
        log.info("Call Save");
    }

    @Override
    public void find() {
        log.info("Call Find");
    }
}
