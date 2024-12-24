<!-- SensorTable.vue -->
<template>
  <div class="min-h-screen bg-gray-100 p-8">
    <div class="bg-white rounded-lg shadow-lg overflow-hidden">
      <!-- Connection Status -->
      <div class="px-6 py-3 flex items-center justify-between border-b border-gray-200">
        <h2 class="text-xl font-semibold text-gray-800">Sensor Data (Realtime)</h2>
        <div class="flex items-center">
          <span class="mr-2 text-sm">Connection Status:</span>
          <span 
            :class="{
              'bg-green-500': isConnected,
              'bg-red-500': !isConnected
            }"
            class="inline-block w-3 h-3 rounded-full"
          ></span>
        </div>
      </div>

      <!-- Table Container -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Org Time</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Col Time</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tag ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Std ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Value</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sensor Type</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Gen ID</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="(item,index) in messages.payload" :key="index" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(item.org_time) }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(item.col_time) }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.tag_ID }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.std_ID }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.value }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                    {{ item.sensor_type }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ item.gen_ID }}</td>
              </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'
import { format } from 'date-fns'
import { useStompWebSocket } from '../useWebSocket';

// Khởi tạo STOMP client
const { isConnected, messages, connect, disconnect } = useStompWebSocket('http://localhost:9999')
setTimeout(()=>{
  console.log(messages.payload)

},10000)



// Format date for display
const formatDate = (dateString) => {
  try {
    // Parse the string into an array of numbers
    const dateArray = JSON.parse(dateString);

    // Create a Date object from the array [year, month, day, hour, minute, second, nanosecond]
    const date = new Date(
      dateArray[0],            // year
      dateArray[1],        // month (0-based)
      dateArray[2],            // day
      dateArray[3],            // hour
      dateArray[4],            // minute
      dateArray[5],            // second
      Math.floor(dateArray[6] / 1000000) // millisecond (converted from nanoseconds)
    );

    return format(date, 'yyyy-MM-dd HH:mm:ss');
  } catch (error) {
    console.error('Invalid date format:', error);
    return dateString;
  }
}


// Connect when component is mounted
onMounted(() => {
  connect()
})
</script>

<style scoped>
.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>