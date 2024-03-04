var form = new Vue({
    el: '#form',
    data: {
        simulationDTO: JSON.parse(document.getElementById('simulationDTOJson').getAttribute('data-simulation-dto')),
        showTableHost: true,
        showTableCloudlet: true,
        showTableVm: true,
        showTableDatacenter: true
        //TODO Доделать тут формирование таблицы в ЛК
    },
    methods: {
        submitForm: function () {
            console.log(this.simulationDTO)
            var formData = JSON.stringify(this.simulationDTO);
            var csrfToken = document.querySelector('input[name="_csrf"]').value;
            var form = document.getElementById('simulationForm');
            var actionUrl = form.getAttribute('action');

            if (this.validateInputs()) {
                // Отправка данных на сервер с помощью Axios
                axios.post(actionUrl, formData, {
                    headers: {
                        'X-CSRF-TOKEN': csrfToken,
                        'Content-Type': 'application/json'
                    }
                })
                    .then((response) => {
                        // Обработка успешного ответа от сервера
                        console.log(response.data);
                        this.simulationDTO = response.data; // Здесь this будет ссылаться на ваш объект класса или компонента
                    })
                    .catch((error) => {
                        // Обработка ошибки
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
                var totalVmCount = 0; // Добавляем переменную для хранения общего количества VM
                // Суммирование параметров всех VM для текущего хоста
                for (var j = 0; j < host.vmDTOS.length; j++) {
                    var vm = host.vmDTOS[j];
                    totalVmCount += vm.vmCount; // Учитываем количество VM
                    totalVmPes += vm.vmPes * vm.vmCount; // Учитываем количество VM
                    totalVmRam += vm.vmRam * vm.vmCount; // Учитываем количество VM
                    totalVmBw += vm.vmBw * vm.vmCount; // Учитываем количество VM
                    totalVmStorage += vm.vmStorage * vm.vmCount; // Учитываем количество VM
                }

                console.log(totalVmPes)
                console.log(totalVmRam)
                console.log(totalVmBw)
                console.log(totalVmStorage)

                console.log("")
                console.log("")
                console.log("")

                console.log(host.hostPes)
                console.log(host.hostRam)
                console.log(host.hostBw)
                console.log(host.hostStorage)
                // Проверка, что сумма параметров VM не превышает параметры хоста
                if (totalVmPes > host.hostPes) { // Учитываем количество хостов
                    alert('Суммарное количество процессоров виртуальных машин не должно превышать количество процессоров хоста');
                    return false; // Возвращаем false, если проверка не пройдена
                }
                if (totalVmRam > host.hostRam) { // Учитываем количество хостов
                    alert('Суммарный объем RAM виртуальных машин не должен превышать объем RAM хоста');
                    return false;
                }
                if (totalVmBw > host.hostBw) { // Учитываем количество хостов
                    alert('Суммарная пропускная способность виртуальных машин не должна превышать пропускную способность хоста');
                    return false;
                }
                if (totalVmStorage > host.hostStorage) { // Учитываем количество хостов
                    alert('Суммарный объем хранилища виртуальных машин не должен превышать объем хранилища хоста');
                    return false;
                }

            }
            return true; // Возвращаем true, если все проверки пройдены успешно
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
        // Метод для добавления копии хоста по индексу
        addHostCopy: function (index) {
            var hostToCopy = this.simulationDTO.hostDTOS[index];
            var newHost = Object.assign({}, hostToCopy); // Создаем копию хоста
            // Создаем новый массив vmDTOS для нового хоста
            newHost.vmDTOS = hostToCopy.vmDTOS.map(vm => Object.assign({}, vm));
            // Вставляем копию после текущего хоста
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
        // Метод для добавления копии datacenter по индексу
        addDatacenterCopy: function (index) {
            var datacenterToCopy = this.simulationDTO.datacenterDTOS[index];
            var newDatacenter = Object.assign({}, datacenterToCopy); // Создаем копию datacenter
            // Вставляем копию после текущего datacenter
            this.simulationDTO.datacenterDTOS.splice(index + 1, 0, newDatacenter);
        },


        addCloudlet: function () {
            if (!this.showTableCloudlet) {
                this.showTableCloudlet = true
            }
            var newCloudlet = {
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

        // Метод для добавления копии cloudlet по индексу
        addCloudletCopy: function (index) {
            var cloudletToCopy = this.simulationDTO.cloudletDTOS[index];
            var newCloudlet = Object.assign({}, cloudletToCopy); // Создаем копию cloudlet
            // Вставляем копию после текущего cloudlet
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
        // Метод для добавления копии VM по индексу хоста и VM
        addVmCopy: function (hostIndex, vmIndex) {
            var vmToCopy = this.simulationDTO.hostDTOS[hostIndex].vmDTOS[vmIndex];
            var newVm = Object.assign({}, vmToCopy); // Создаем копию VM
            // Вставляем копию после текущей VM
            this.simulationDTO.hostDTOS[hostIndex].vmDTOS.splice(vmIndex + 1, 0, newVm);
        },
        // Метод для определения, нужно ли показывать таблицу VM для указанного хоста
        shouldShowVmTable: function (host) {
            return host.vmDTOS.length > 0;
        }

    },
    mounted: function () {
        // После загрузки компонента добавим пустые объекты в списки

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
            default:

                break;
        }
    }
});
