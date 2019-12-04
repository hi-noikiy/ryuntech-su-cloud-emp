import request from '@/utils/request'

export function getOverdueConfigMessage() {
  return request({
    url: '/saas/planOverdueRemind/getConfig',
    method: 'get',
  })
}

export function updateOverdueConfigMessage(data) {
  return request({
    url: '/saas/planOverdueRemind/config',
    method: 'post',
    data
  })
}

export function getExpireConfigMessage() {
  return request({
    url: '/saas/planExpireRemind/getConfig',
    method: 'get',
  })
}

export function updateExpireConfigMessage(data) {
  return request({
    url: '/saas/planExpireRemind/config',
    method: 'post',
    data
  })
}