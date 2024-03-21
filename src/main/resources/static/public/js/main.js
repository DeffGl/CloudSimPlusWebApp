var form = new Vue({
    el: '#form',
    data: {
        simulationDTO: JSON.parse(document.getElementById('simulationDTOJson').getAttribute('data-simulation-dto')),
        showTableHost: true,
        showTableCloudlet: true,
        showTableVm: true,
        showTableDatacenter: true,
        currentPage: 0,
        totalPages: null,
        globalCounter: 0
    },
    methods: {
        loadPage(page) {

            if (page < 0 || page >= this.totalPages) {
                console.log("Запрашиваемая страница вне диапазона.");
                return; // Не отправлять запрос, если страница вне диапазона
            }
            this.globalCounter = page * this.simulationDTO.length;

            console.log("prev currentPage" + this.currentPage)
            console.log("prev totalPages" + this.totalPages)
            axios.get('/rest/account?page=' + page)
                .then(response => {
                    this.currentPage = response.data.page;
                    this.totalPages = response.data.totalPages;
                    this.simulationDTO = response.data.simulationDTOS; // Если вам нужно обновить данные
                    console.log("pos currentPage" + this.currentPage)
                    console.log("pos totalPages" + this.totalPages)
                    console.log(this.simulationDTO)
                })
                .catch(error => {
                    console.error('Ошибка при загрузке данных:', error);
                });
        },
        submitForm: function () {
            console.log(this.simulationDTO)
            var formData = JSON.stringify(this.simulationDTO);
            var csrfToken = document.querySelector('input[name="_csrf"]').value;
            var form = document.getElementById('simulationForm');
            var actionUrl = form.getAttribute('action');

            if (this.validateInputs()) {
                axios.post(actionUrl, formData, {
                    headers: {
                        'X-CSRF-TOKEN': csrfToken,
                        'Content-Type': 'application/json'
                    }
                })
                    .then((response) => {
                        console.log(response.data);
                        this.simulationDTO = response.data;
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            }
        },

        validateInputs: function () {
            //TODO Возможно доделать валидацию по времени выполнения lifetime, надо будет посмотреть
            // Выполнение валидации
            for (var i = 0; i < this.simulationDTO.hostDTOS.length; i++) {
                var host = this.simulationDTO.hostDTOS[i];
                var totalVmPes = 0;
                var totalVmRam = 0;
                var totalVmBw = 0;
                var totalVmStorage = 0;
                var totalVmCount = 0;

                for (var j = 0; j < host.vmDTOS.length; j++) {
                    var vm = host.vmDTOS[j];
                    totalVmCount += vm.vmCount; // Учитываем количество VM
                    totalVmPes += vm.vmPes * vm.vmCount; // Учитываем количество VM
                    totalVmRam += vm.vmRam * vm.vmCount; // Учитываем количество VM
                    totalVmBw += vm.vmBw * vm.vmCount; // Учитываем количество VM
                    totalVmStorage += vm.vmStorage * vm.vmCount; // Учитываем количество VM
                }


                if (totalVmPes > host.hostPes) {
                    alert('Суммарное количество процессоров виртуальных машин не должно превышать количество процессоров хоста');
                    return false;
                }
                if (totalVmRam > host.hostRam) {
                    alert('Суммарный объем RAM виртуальных машин не должен превышать объем RAM хоста');
                    return false;
                }
                if (totalVmBw > host.hostBw) {
                    alert('Суммарная пропускная способность виртуальных машин не должна превышать пропускную способность хоста');
                    return false;
                }
                if (totalVmStorage > host.hostStorage) {
                    alert('Суммарный объем хранилища виртуальных машин не должен превышать объем хранилища хоста');
                    return false;
                }

            }
            return true;
        },


        addHost: function () {
            if (!this.showTableHost) {
                this.showTableHost = true
            }
            var newHost = {
                hostCount: '',
                hostPes: '',
                hostMips: '',
                hostRam: '',
                hostBw: '',
                hostStorage: '',
                vmDTOS: [{
                    vmLifetime: '',
                    vmCount: '',
                    vmPes: '',
                    vmRam: '',
                    vmBw: '',
                    vmStorage: ''
                }]
            };
            this.simulationDTO.hostDTOS.push(newHost);
        },
        removeHost: function (index) {
            this.simulationDTO.hostDTOS.splice(index, 1);
            if (this.simulationDTO.hostDTOS.length === 0) {
                this.showTableHost = false;
            }
        },
        addHostCopy: function (index) {
            var hostToCopy = this.simulationDTO.hostDTOS[index];
            var newHost = Object.assign({}, hostToCopy); // Создаем копию хоста

            newHost.vmDTOS = hostToCopy.vmDTOS.map(vm => Object.assign({}, vm));

            this.simulationDTO.hostDTOS.splice(index + 1, 0, newHost);
        },

        addDatacenter: function () {
            if (!this.showTableDatacenter) {
                this.showTableDatacenter = true;
            }
            var newDatacenter = {
                datacenterCount: '',
                schedulingInterval: ''
            };
            this.simulationDTO.datacenterDTOS.push(newDatacenter);
        },
        removeDatacenter: function (index) {
            this.simulationDTO.datacenterDTOS.splice(index, 1);
            if (this.simulationDTO.datacenterDTOS.length === 0) {
                this.showTableDatacenter = false;
            }
        },
        addDatacenterCopy: function (index) {
            var datacenterToCopy = this.simulationDTO.datacenterDTOS[index];
            var newDatacenter = Object.assign({}, datacenterToCopy);

            this.simulationDTO.datacenterDTOS.splice(index + 1, 0, newDatacenter);
        },


        addCloudlet: function () {
            if (!this.showTableCloudlet) {
                this.showTableCloudlet = true
            }
            var newCloudlet = {
                cloudletMaxExecutionTime: '',
                cloudletLifetime: '',
                cloudletCount: '',
                cloudletPes: '',
                cloudletLength: '',
                cloudletSize: ''
            };
            this.simulationDTO.cloudletDTOS.push(newCloudlet);
        },
        removeCloudlet: function (index) {
            this.simulationDTO.cloudletDTOS.splice(index, 1);
            if (this.simulationDTO.cloudletDTOS.length === 0) {
                this.showTableCloudlet = false;
            }
        },
        addCloudletCopy: function (index) {
            var cloudletToCopy = this.simulationDTO.cloudletDTOS[index];
            var newCloudlet = Object.assign({}, cloudletToCopy);

            this.simulationDTO.cloudletDTOS.splice(index + 1, 0, newCloudlet);
        },


        addVm: function (index) {
            if (!this.showTableVm) {
                this.showTableVm = true
            }
            var newVm = {
                vmLifetime: '',
                vmCount: '',
                vmPes: '',
                vmRam: '',
                vmBw: '',
                vmStorage: ''
            };
            this.simulationDTO.hostDTOS[index].vmDTOS.push(newVm);
        },
        removeVm: function (index, vmIndex) {
            this.simulationDTO.hostDTOS[index].vmDTOS.splice(vmIndex, 1);
            if (this.simulationDTO.hostDTOS[index].vmDTOS.length === 0) {
                this.showTableVm = false;
            }
        },
        addVmCopy: function (hostIndex, vmIndex) {
            var vmToCopy = this.simulationDTO.hostDTOS[hostIndex].vmDTOS[vmIndex];
            var newVm = Object.assign({}, vmToCopy); // Создаем копию VM

            this.simulationDTO.hostDTOS[hostIndex].vmDTOS.splice(vmIndex + 1, 0, newVm);
        },
        shouldShowVmTable: function (host) {
            return host.vmDTOS.length > 0;
        },

        repeatSimulation: function (simulation){
            var actionUrl = simulation.actionUrl + "?simulationId=" + simulation.simulationId;
            console.log(simulation);
            console.log(actionUrl);
            window.location.href = actionUrl;
        }

    },
    mounted: function () {

        var totPageEl = document.getElementById('totalPages');

        if (totPageEl != null){
            this.totalPages = totPageEl.getAttribute('total-pages');
        }


        switch (this.simulationDTO.simulationType) {
            case "BASIC_SIMULATION":
                this.simulationDTO.hostDTOS.push({
                    hostCount: 2,
                    hostPes: 8,
                    hostMips: 1000,
                    hostRam: 2048,
                    hostBw: 10000,
                    hostStorage: 1000000,
                    vmDTOS: [{
                        vmCount: 2,
                        vmPes: 4,
                        vmRam: 512,
                        vmBw: 1000,
                        vmStorage: 10000
                    }]
                });
                this.simulationDTO.cloudletDTOS.push({
                    cloudletCount: 4,
                    cloudletPes: 2,
                    cloudletLength: 10000,
                    cloudletSize: 1024
                });
                break;
            case "LIFETIME_SIMULATION":
                this.simulationDTO.hostDTOS.push({
                    hostCount: 3,
                    hostPes: 16,
                    hostMips: 1000,
                    hostRam: 2048,
                    hostBw: 10000,
                    hostStorage: 1000000,
                    vmDTOS: [{
                        vmLifetime: 3,
                        vmCount: 4,
                        vmPes: 4,
                        vmRam: 512,
                        vmBw: 1000,
                        vmStorage: 10000
                    }]
                });
                this.simulationDTO.cloudletDTOS.push({
                    cloudletLifetime: 5,
                    cloudletCount: 4,
                    cloudletPes: 2,
                    cloudletLength: 10000,
                    cloudletSize: 1024
                });

                this.simulationDTO.datacenterDTOS.push({
                    datacenterCount: 1,
                    schedulingInterval: 3
                });
                break;
            case "CANCEL_SIMULATION":
                this.simulationDTO.hostDTOS.push({
                    hostCount: 3,
                    hostPes: 16,
                    hostMips: 1000,
                    hostRam: 2048,
                    hostBw: 10000,
                    hostStorage: 1000000,
                    vmDTOS: [{
                        vmCount: 4,
                        vmPes: 4,
                        vmRam: 512,
                        vmBw: 1000,
                        vmStorage: 10000
                    }]
                });
                this.simulationDTO.cloudletDTOS.push({
                    cloudletMaxExecutionTime: 10,
                    cloudletCount: 4,
                    cloudletPes: 2,
                    cloudletLength: 10000,
                    cloudletSize: 1024
                });

                this.simulationDTO.datacenterDTOS.push({
                    datacenterCount: 1,
                    schedulingInterval: 3
                });
                break;
            default:

                break;
        }
    }
});
