import NextAuth, { User } from 'next-auth'
import GoogleProvider from 'next-auth/providers/google'
export const authOptions = {
  providers: [
    GoogleProvider({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
      profile(profile) {
        return {
          id: profile.sub,
          name: profile.name,
          email: profile.email,
          image: profile.picture
        }
      }
    })
  ],
  callbacks: {
    async signIn({ user }: { user: User }) {
      const { email, name } = user;

      try {
        const response = await fetch('http://localhost:8080/api/users/findOrCreate', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email,
            username: name,
            firstName: name?.split(' ')[0] || '',
            lastName: name?.split(' ').slice(1).join(' ') || '',
          }),
        });

        if (!response.ok) {
          console.error('Failed to create or retrieve user.');
          return false;
        }

        return true;
      } catch (error) {
        console.error('Error during sign-in process:', error);
        return false;
      }
    },
  },
}
export const {
  handlers: { GET, POST },
  auth
} = NextAuth(authOptions)