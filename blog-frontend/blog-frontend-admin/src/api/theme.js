import service from '@/utils/service'

const baseUrl = '/api/admin/themes'

const themeApi = {}

themeApi.getProperty = () => {
  return service({
    url: `${baseUrl}`,
    method: 'get'
  })
}

themeApi.listConfigurations = () => {
  return service({
    url: `${baseUrl}/configurations`,
    method: 'get'
  })
}

themeApi.getSettings = () => {
  return service({
    url: `${baseUrl}/settings`,
    method: 'get'
  })
}

themeApi.saveSettings = settings => {
  return service({
    url: `${baseUrl}/settings`,
    data: settings,
    method: 'post'
  })
}

themeApi.fetching = url => {
  return service({
    url: `${baseUrl}/fetching`,
    timeout: 60000,
    params: {
      uri: url
    },
    method: 'post'
  })
}

themeApi.fetchingBranches = url => {
  return service({
    url: `${baseUrl}/fetchingBranches`,
    timeout: 60000,
    params: {
      uri: url
    },
    method: 'post'
  })
}

themeApi.fetchingReleases = url => {
  return service({
    url: `${baseUrl}/fetchingReleases`,
    timeout: 60000,
    params: {
      uri: url
    },
    method: 'post'
  })
}

themeApi.fetchingBranch = (url, branchName) => {
  return service({
    url: `${baseUrl}/fetchBranch`,
    timeout: 60000,
    params: {
      uri: url,
      branch: branchName
    },
    method: 'get'
  })
}

themeApi.fetchingLatestRelease = url => {
  return service({
    url: `${baseUrl}/fetchLatestRelease`,
    timeout: 60000,
    params: {
      uri: url
    },
    method: 'get'
  })
}

themeApi.fetchingRelease = (url, tagName) => {
  return service({
    url: `${baseUrl}/fetchingRelease`,
    timeout: 60000,
    params: {
      uri: url,
      tag: tagName
    },
    method: 'get'
  })
}

themeApi.getContent = path => {
  return service({
    url: `${baseUrl}/files/content`,
    params: {
      path: path
    },
    method: 'get'
  })
}

themeApi.getContent = (themeId, path) => {
  return service({
    url: `${baseUrl}/${themeId}/files/content`,
    params: {
      path: path
    },
    method: 'get'
  })
}

themeApi.saveContent = (path, content) => {
  return service({
    url: `${baseUrl}/files/content`,
    data: {
      path: path,
      content: content
    },
    method: 'put'
  })
}

themeApi.saveContent = (themeId, path, content) => {
  return service({
    url: `${baseUrl}/${themeId}/files/content`,
    data: {
      path: path,
      content: content
    },
    method: 'put'
  })
}

themeApi.reload = () => {
  return service({
    url: `${baseUrl}/reload`,
    method: 'post'
  })
}

themeApi.exists = template => {
  return service({
    url: `${baseUrl}/activation/template/exists`,
    method: 'get',
    params: {
      template: template
    }
  })
}

export default themeApi
