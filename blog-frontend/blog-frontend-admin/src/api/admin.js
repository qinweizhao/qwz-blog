import service from '@/utils/service'

const baseUrl = '/api/admin'

const adminApi = {}

adminApi.counts = () => {
  return service({
    url: `${baseUrl}/counts`,
    method: 'get'
  })
}

adminApi.environments = () => {
  return service({
    url: `${baseUrl}/environments`,
    method: 'get'
  })
}

adminApi.login = (username, password, authcode) => {
  return service({
    url: `${baseUrl}/login`,
    data: {
      username: username,
      password: password,
      authcode: authcode
    },
    method: 'post'
  })
}

adminApi.logout = () => {
  return service({
    url: `${baseUrl}/logout`,
    method: 'post'
  })
}

adminApi.refreshToken = refreshToken => {
  return service({
    url: `${baseUrl}/refresh/${refreshToken}`,
    method: 'post'
  })
}

adminApi.sendResetCode = param => {
  return service({
    url: `${baseUrl}/password/code`,
    data: param,
    method: 'post'
  })
}

adminApi.resetPassword = param => {
  return service({
    url: `${baseUrl}/password/reset`,
    data: param,
    method: 'put'
  })
}

adminApi.updateAdminAssets = () => {
  return service({
    url: `${baseUrl}/halo-admin`,
    method: 'put',
    timeout: 600 * 1000
  })
}

adminApi.getLogFiles = lines => {
  return service({
    url: `${baseUrl}/blog/logfile`,
    params: {
      lines: lines
    },
    method: 'get'
  })
}

adminApi.downloadLogFiles = lines => {
  return service({
    url: `${baseUrl}/halo/logfile/download`,
    params: {
      lines: lines
    },
    method: 'get'
  })
}

export default adminApi
