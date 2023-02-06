import { shallowMount } from '@vue/test-utils'
import ModelViewer from '@/components/ModelViewer.vue'

describe('ModelViewer.vue', () => {
  it('renders props.msg when passed', () => {
    const msg = 'new message'
    const wrapper = shallowMount(ModelViewer, {
      props: { msg }
    })
    expect(wrapper.text()).toMatch(msg)
  })
})
