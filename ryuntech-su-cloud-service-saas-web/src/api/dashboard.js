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
// 部门层级关系
export function departrelation() {
  return request({
    url: '/saas/index/departrelation',
    method: 'get'
  })
}

// 获取数据简报
export function queryDataBriefing(data) {
  return request({
    url: '/saas/index/dataBriefing',
    method: 'post',
    data
  })
}

//  页面显示时根据根部门递归遍历所有部门id
export function diguiDepartmentIds(departData, arr = []) {
  for (var i = 0; i < departData.length; i++) {
    let sonList = departData[i].sonDepartment;
    if(sonList && sonList.length > 0) {
      arr.push(departData[i].departmentName);
      diguiDepartmentIds(sonList, arr);
    } else {
      arr.push(departData[i].departmentName);
    }
  }
  return arr;
}