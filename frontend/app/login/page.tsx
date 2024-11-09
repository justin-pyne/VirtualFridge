import { auth } from '@/auth'
import { AuthButton } from '@/components/AuthButton'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import { redirect } from 'next/navigation'

export default async function LoginPage() {  
  const session = await auth()
  if (session) {
    redirect('/')
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <Card className="w-[400px]">
        <CardHeader>
          <CardTitle>Welcome to VirtualFridge</CardTitle>
          <CardDescription>Please sign in with your Google account</CardDescription>
        </CardHeader>
        <CardContent>
          <AuthButton type="signin"/>
        </CardContent>
      </Card>
    </div>
  )
}