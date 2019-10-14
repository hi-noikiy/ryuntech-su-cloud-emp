import request from '@/utils/request'

export function reportdata(data) {
  return request({
    url: '/saas/index/reportdata',
    method: 'post',
    data
  })
}

export function reportback() {
  return request({
    url: '/saas/index/reportback',
    method: 'get'
  })
}
