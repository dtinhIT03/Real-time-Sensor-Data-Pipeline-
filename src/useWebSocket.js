// useStompWebSocket.js
import { ref, onUnmounted, reactive } from 'vue'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

export function useStompWebSocket(serverUrl) {
  const isConnected = ref(false)
  const stompClient = ref(null)
  const messages = reactive({
    payload : []
  })

  const connect = () => {
    const socket = new SockJS(`${serverUrl}/ws`)
    
    stompClient.value = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.debug(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        isConnected.value = true
        console.log('STOMP connection established')
        subscribe()
      },
      onDisconnect: () => {
        isConnected.value = false
        console.log('STOMP disconnected')
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame)
      }
    })

    stompClient.value.activate()
  }

  const subscribe = () => {
    if (!stompClient.value) return

    stompClient.value.subscribe('/sensor', (message) => {
      try {
        const payload = JSON.parse(message.body)
        messages.payload = payload;
        console.log(messages)
        
        
        
      } catch (error) {
        console.error('Error parsing message:', error)
      }
    })
  }

  const disconnect = () => {
    if (stompClient.value) {
      stompClient.value.deactivate()
      stompClient.value = null
    }
    isConnected.value = false
  }

  onUnmounted(() => {
    disconnect()
  })

  return {
    isConnected,
    messages,
    connect,
    disconnect
  }
}