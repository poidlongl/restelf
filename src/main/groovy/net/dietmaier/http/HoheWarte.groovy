package net.dietmaier.http

class HoheWarte extends Service {
    static HoheWarte getInstance() {
        def instance = new HoheWarte()
        instance.defaultRequest.url = 'https://www.data.gv.at/'
        instance
    }

    def getMetadata() {
        get {
            path = 'katalog/api/3/action/package_show'
            query = ['id': '69a06550-1ede-4f50-9c36-e7fb5cf6e7e8']
        }.fail { it.raiseException() }.value
    }

    def getCurrent() {
        get {
            url = 'https://www.wien.gv.at/gogv/l9viebdleclwea1872f'
        }.fail { it.raiseException() }.value
    }
}
