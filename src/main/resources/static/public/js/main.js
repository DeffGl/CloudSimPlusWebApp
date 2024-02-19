var form = new Vue({
    el: '#form',
    data: {
        simulationDTO: JSON.parse(document.getElementById('simulationDTOJson').getAttribute('data-simulation-dto')),
        showTableHost: true,
        showTableCloudlet: true,
        showTableVm: true
    },
    methods: {
        submitForm: function () {
            console.log(this.simulationDTO)
            var formData = JSON.stringify(this.simulationDTO);
            var csrfToken = document.querySelector('input[name="_csrf"]').value;

            // Отправка данных на сервер с помощью Axios
            axios.post('/simulation/start', formData, {
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
        },

        addHost: function () {
            if (!this.showTableHost){
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
        addHostCopy: function(index) {
            var hostToCopy = this.simulationDTO.hostDTOS[index];
            var newHost = Object.assign({}, hostToCopy); // Создаем копию хоста
            // Создаем новый массив vmDTOS для нового хоста
            newHost.vmDTOS = hostToCopy.vmDTOS.map(vm => Object.assign({}, vm));
            // Вставляем копию после текущего хоста
            this.simulationDTO.hostDTOS.splice(index + 1, 0, newHost);
        },


        addCloudlet: function () {
            if (!this.showTableCloudlet){
                this.showTableCloudlet = true
            }
            var newCloudlet = {
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
        addCloudletCopy: function(index) {
            var cloudletToCopy = this.simulationDTO.cloudletDTOS[index];
            var newCloudlet = Object.assign({}, cloudletToCopy); // Создаем копию cloudlet
            // Вставляем копию после текущего cloudlet
            this.simulationDTO.cloudletDTOS.splice(index + 1, 0, newCloudlet);
        },


        addVm: function (index) {
            if (!this.showTableVm){
                this.showTableVm = true
            }
            var newVm = {
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
        addVmCopy: function(hostIndex, vmIndex) {
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
    }
});
