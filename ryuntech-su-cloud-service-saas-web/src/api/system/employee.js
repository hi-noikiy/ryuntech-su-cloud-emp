import request from '@/utils/request'

export function getEmployeeList(query, data) {
  return request({
    url: '/saas/employee/list?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}
